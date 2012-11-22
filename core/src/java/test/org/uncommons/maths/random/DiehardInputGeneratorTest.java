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

import java.io.File;
import org.testng.annotations.Test;

/**
 * Unit test for the {@link DiehardInputGenerator} class.
 * @author Daniel Dyer
 */
public class DiehardInputGeneratorTest
{
    /**
     * Make sure that the input file is created and that it is the correct size.
     */
    @Test
    public void testFileCreation() throws Exception
    {
        File tempFile = File.createTempFile("diehard-input", null);
        try
        {
            DiehardInputGenerator.main(new String[]{"java.util.Random", tempFile.getAbsolutePath()});
            assert tempFile.length() == 12000000 : "Generated file should be 12Mb, is " + tempFile.length() + " bytes.";
        }
        finally
        {
            if (!tempFile.delete())
            {
                tempFile.deleteOnExit();
            }
        }
    }
}
