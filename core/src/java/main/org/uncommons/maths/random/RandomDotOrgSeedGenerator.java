// ============================================================================
//   Copyright 2006, 2007, 2008 Daniel W. Dyer
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Connects to the <a href="http://www.random.org" target="_top">random.org</a>
 * website and downloads a set of random bits to use as seed data.  It is generally
 * better to use the {@link DevRandomSeedGenerator} where possible, as it should be
 * much quicker. This seed generator is most useful on Microsoft Windows
 * and other platforms that do not provide {@literal /dev/random}.
 * @author Daniel Dyer
 */
public class RandomDotOrgSeedGenerator implements SeedGenerator
{
    private static final String BASE_URL = "http://www.random.org";

    /** The URL from which the random bytes are retrieved. */
    private static final String RANDOM_URL = BASE_URL + "/cgi-bin/randbyte?nbytes={0,number,0}&format=d";

    /** Used to identify the client to the random.org service. */
    private static final String USER_AGENT = RandomDotOrgSeedGenerator.class.getName();

    private static final Lock cacheLock = new ReentrantLock();
    private static byte[] cache = new byte[1024];
    private static int cacheOffset = cache.length;

    /**
     * {@inheritDoc}
     */    
    public byte[] generateSeed(int length) throws SeedException
    {
        byte[] seedData = new byte[length];
        try
        {
            cacheLock.lock();
            int count = 0;
            while (count < length)
            {
                if (cacheOffset < cache.length)
                {
                    int numberOfBytes = Math.min(length - count, cache.length - cacheOffset);
                    System.arraycopy(cache, cacheOffset, seedData, count, numberOfBytes);
                    count += numberOfBytes;
                    cacheOffset += numberOfBytes;
                }
                else
                {
                    refreshCache(length - count);
                }
            }
        }
        catch (IOException ex)
        {
            throw new SeedException("Failed downloading bytes from " + BASE_URL, ex);
        }
        catch (SecurityException ex)
        {
            // Might be thrown if resource access is restricted (such as in an applet sandbox).
            throw new SeedException("SecurityManager prevented access to " + BASE_URL, ex);
        }
        finally
        {
            cacheLock.unlock();
        }
        return seedData;
    }


    /**
     * @param minimumBytes The minimum number of bytes to request from random.org.  The
     * implementation may request more and cache the excess (to avoid making lots of
     * small requests).
     * @throws IOException If there is a problem downloading the random bits.
     */
    private void refreshCache(int minimumBytes) throws IOException
    {
        int numberOfBytes = Math.max(minimumBytes, cache.length);
        if (numberOfBytes > cache.length)
        {
            cache = new byte[numberOfBytes];
        }
        URL url = new URL(MessageFormat.format(RANDOM_URL, numberOfBytes));
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        Reader reader = new InputStreamReader(connection.getInputStream());
        
        try
        {
            StreamTokenizer tokenizer = new StreamTokenizer(reader);

            int index = -1;
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF)
            {
                if (tokenizer.ttype != StreamTokenizer.TT_NUMBER)
                {
                    throw new IOException("Received invalid data.");
                }
                cache[++index] = (byte) tokenizer.nval;
            }
            if (index < cache.length - 1)
            {
                throw new IOException("Insufficient data received.");
            }
            cacheOffset = 0;
        }
        finally
        {
            reader.close();
        }
    }


    @Override
    public String toString()
    {
        return BASE_URL;
    }
}
