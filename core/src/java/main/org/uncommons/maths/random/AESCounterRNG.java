// ============================================================================
//   Copyright 2006-2012 Daniel W. Dyer
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// ============================================================================
package org.uncommons.maths.random;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import javax.crypto.Cipher;
import javax.crypto.SecretKeySpec;
import org.uncommons.maths.binary.BinaryUtils;

/**
 * <p>Non-linear random number generator based on the AES block cipher in counter mode.
 * Uses the seed as a key to encrypt a 128-bit counter using AES(Rijndael).</p>
 * 
 * <p>By default, we only use a 128-bit key for the cipher because any larger key requires
 * the inconvenience of installing the unlimited strength cryptography policy
 * files for the Java platform.  Larger keys may be used (192 or 256 bits) but if the
 * cryptography policy files are not installed, a
 * {@link java.security.GeneralSecurityException} will be thrown.</p>
 *
 * <p><em>NOTE: Because instances of this class require 128-bit seeds, it is not
 * possible to seed this RNG using the {@link #setSeed(long)} method inherited
 * from {@link Random}.  Calls to this method will have no effect.
 * Instead the seed must be set by a constructor.</em></p>
 *
 * <p><em>NOTE: THIS CLASS IS NOT SERIALIZABLE</em></p>
 * 
 * @author Daniel Dyer
 */
public class AESCounterRNG extends Random implements RepeatableRNG
{
    private static final long serialVersionUID = 5949778642428995210L;

    private static final int DEFAULT_SEED_SIZE_BYTES = 16;
    
    /**
     * 128-bit counter. Note to forkers: when running a cipher in ECB mode, this
     * counter's length should equal the cipher's block size.
     */
    private static final int COUNTER_SIZE_BYTES = 16;
    
    /**
     * Number of blocks to encrypt at once, to construct/GC fewer arrays. This
     * takes advantage of the fact that in ECB mode, concatenating and then
     * encrypting gives the same output as encrypting and then concatenating, as
     * long as both plaintexts are a whole number of blocks. (The AES block size
     * is 128 bits at all key lengths.)
     */
    private static final int BLOCKS_AT_ONCE = 16;

    /**
     * If the seed is longer than this, part of it becomes the counter's initial
     * value. Otherwise, the full seed becomes the AES key and the counter is
     * initially zero. Package-visible for testing of its initialization.
     */
    static final int MAX_KEY_LENGTH_BYTES;
    static {
        try {
            MAX_KEY_LENGTH_BYTES =
                    Math.min(Cipher.getMaxAllowedKeyLength("AES"), 32);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private final byte[] seed;
    private transient Cipher cipher;
    private final byte[] counter = new byte[COUNTER_SIZE_BYTES];
    private transient byte[] counterInput;

    /** Called in constructor and readObject to initialize transient fields. */
    protected void initTransientFields() throws GeneralSecurityException {
      lock = new ReentrantLock();
      cipher = Cipher.getInstance("AES/ECB/NoPadding");
      cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(this.seed, "AES"));
      counterInput = new byte[COUNTER_SIZE_BYTES * BLOCKS_AT_ONCE];
    }
    
    /** Needed to initialize transient fields when deserializing. */
    private void readObject(ObjectInputStream in)
        throws IOException,ClassNotFoundException {
      in.defaultReadObject();
      try {
        initTransientFields();
      } catch (GeneralSecurityException e) {
        throw new IOException(e);
      }
    }

    // Lock to prevent concurrent modification of the RNG's internal state.
    private transient ReentrantLock lock;

    private byte[] currentBlock = new byte[COUNTER_SIZE_BYTES * BLOCKS_AT_ONCE];
    
    // force generation of first block on demand
    private int index = currentBlock.length;


    /**
     * Creates a new RNG and seeds it using 128 bits from the default seeding strategy.
     * @throws GeneralSecurityException If there is a problem initialising the AES cipher.
     */
    public AESCounterRNG() throws GeneralSecurityException
    {
        this(DEFAULT_SEED_SIZE_BYTES);
    }


    /**
     * Seed the RNG using the provided seed generation strategy to create a 128-bit
     * seed.
     * @param seedGenerator The seed generation strategy that will provide
     * the seed value for this RNG.
     * @throws SeedException If there is a problem generating a seed.
     * @throws GeneralSecurityException If there is a problem initialising the AES cipher.
     */
    public AESCounterRNG(SeedGenerator seedGenerator) throws SeedException,
                                                             GeneralSecurityException
    {
        this(seedGenerator.generateSeed(DEFAULT_SEED_SIZE_BYTES));
    }


    /**
     * Seed the RNG using the default seed generation strategy to create a seed of the
     * specified size.
     * @param seedSizeBytes The number of bytes to use for seed data.  Valid values
     * are 16 (128 bits), 24 (192 bits) and 32 (256 bits).  Any other values will
     * result in an exception from the AES implementation.
     * @throws GeneralSecurityException If there is a problem initialising the AES cipher.
     * @since 1.0.2
     */
    public AESCounterRNG(int seedSizeBytes) throws GeneralSecurityException
    {
        this(DefaultSeedGenerator.getInstance().generateSeed(seedSizeBytes));
    }


    /**
     * Creates an RNG and seeds it with the specified seed data.
     * @param seed The seed data used to initialise the RNG.
     * @throws GeneralSecurityException If there is a problem initialising the AES cipher.
     */
    public AESCounterRNG(byte[] seed) throws GeneralSecurityException
    {
        if (seed == null)
        {
            throw new IllegalArgumentException("AES RNG requires a 128-bit, 192-bit, 256-bit, 320-bit or 384-bit seed.");
        }
        if (seed.length > MAX_KEY_LENGTH_BYTES) {
            this.seed = Arrays.copyOfRange(
                seed, 0, seed.length - COUNTER_SIZE_BYTES);
            System.arraycopy(
                seed, MAX_KEY_LENGTH_BYTES, counter, 0, COUNTER_SIZE_BYTES);
        } else {
            this.seed = seed.clone();
        }
        initTransientFields();
    }


    /**
     * {@inheritDoc}
     */
    public byte[] getSeed()
    {
        return seed.clone();
    }


    private void incrementCounter()
    {
        for (int i = 0; i < counter.length; i++)
        {
            ++counter[i];
            if (counter[i] != 0) // Check whether we need to loop again to carry the one.
            {
                break;
            }
        }
    }


    /**
     * Generates BLOCKS_AT_ONCE 128-bit (16-byte) blocks. Copies them to
     * currentBlock.
     * @throws GeneralSecurityException If there is a problem with the cipher
     * that generates the random data.
     */
    private void nextBlock() throws GeneralSecurityException
    {
        for (int i=0; i < BLOCKS_AT_ONCE; i++) {
            incrementCounter();
            System.arraycopy(counter, 0, counterInput, i*COUNTER_SIZE_BYTES,
                    COUNTER_SIZE_BYTES);
        }
        System.arraycopy(cipher.doFinal(counterInput), 0, currentBlock, 0,
                COUNTER_SIZE_BYTES * BLOCKS_AT_ONCE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected final int next(int bits)
    {
        int result;
        try
        {
            lock.lock();
            if (currentBlock.length - index < 4)
            {
                try
                {
                    nextBlock();
                    index = 0;
                }
                catch (GeneralSecurityException ex)
                {
                    // Should never happen.  If initialisation succeeds without exceptions
                    // we should be able to proceed indefinitely without exceptions.
                    throw new IllegalStateException("Failed creating next random block.", ex);
                }
            }
            result = BinaryUtils.convertBytesToInt(currentBlock, index);
            index += 4;
        }
        finally
        {
            lock.unlock();
        }
        return result >>> (32 - bits);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AESCounterRNG) {
            return Arrays.equals(seed, ((AESCounterRNG) other).seed);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(seed);
    }
}
