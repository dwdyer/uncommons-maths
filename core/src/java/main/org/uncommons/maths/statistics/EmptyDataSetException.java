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
package org.uncommons.maths.statistics;

/**
 * Unchecked exception thrown when an attempt is made to obtain
 * statistics from a {@link DataSet} that has had no values added
 * to it.
 * @author Daniel Dyer
 */
public class EmptyDataSetException extends RuntimeException
{
    public EmptyDataSetException()
    {
        super("No values in data set.");
    }
}
