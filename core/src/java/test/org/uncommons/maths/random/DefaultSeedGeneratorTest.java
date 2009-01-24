// ============================================================================
//   Copyright 2006-2009 Daniel W. Dyer
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

import java.security.Permission;
import org.testng.annotations.Test;

/**
 * Unit test for {@link DefaultSeedGenerator}.
 * @author Daniel Dyer
 */
public class DefaultSeedGeneratorTest
{
    /**
     * Check that the default seed generator gracefully falls
     * back to an alternative generation strategy when the security
     * manager prevents it from using its first choice.
     */
    @Test
    public void testRestrictedEnvironment()
    {
        SecurityManager securityManager = System.getSecurityManager();
        try
        {
            // Don't allow file system or network access.
            System.setSecurityManager(new RestrictedSecurityManager());
            DefaultSeedGenerator.getInstance().generateSeed(4);
            // Should get to here without exceptions.
        }
        finally
        {
            // Restore the original security manager so that we don't
            // interfere with the running of other tests.
            System.setSecurityManager(securityManager);
        }
    }


    /**
     * This security manager allows everything except for some operations that are
     * explicitly blocked.  These operations are accessing /dev/random and opening
     * a socket connection.
     */
    private static final class RestrictedSecurityManager extends SecurityManager
    {
        @Override
        public void checkRead(String file)
        {
            if (file.equals("/dev/random"))
            {
                throw new SecurityException("Test not permitted to access /dev/random");
            }
        }


        @Override
        public void checkConnect(String host, int port)
        {
            throw new SecurityException("Test not permitted to connect to " + host + ":" + port);
        }


        @Override
        public void checkPermission(Permission permission)
        {
            // Allow everything.
        }
    }
}
