/* Copyright 2011 GOTO Metrics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.gotometrics.hbase.rowkey;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;

/** Serialize and deserialize Float Objects into HBase row keys.
 * The serialization and deserialization method are identical to 
 * {@link FloatWritableRowKey} after converting the FloatWritable to/from a 
 * Float.
 *
 * <h1> Usage </h1>
 * This is the slower class for storing floats. Only one copy is made when 
 * serializing and deserializing, but unfortunately Float objects are 
 * immutable and thus cannot be re-used across multiple deserializations.
 * However, deserialized primitive floats are first passed to 
 * {#Float.valueOf}, so boxed Float values may be shared if the 
 * #valueOf method has frequent cache hits.
 */
public class FloatRowKey extends FloatWritableRowKey 
{
  @Override
  public Class<?> getSerializedClass() { return Float.class; }

  protected Object toFloatWritable(Object o) {
    if (o == null || o instanceof FloatWritable)
      return o;
    return new FloatWritable((Float)o);
  }

  @Override
  public int getSerializedLength(Object o) throws IOException {
    return super.getSerializedLength(toFloatWritable(o));
  }

  @Override
  public void serialize(Object o, ImmutableBytesWritable w) throws IOException {
    super.serialize(toFloatWritable(o), w);
  }

  @Override
  public Object deserialize(ImmutableBytesWritable w) throws IOException {
    FloatWritable fw = (FloatWritable) super.deserialize(w);
    if (fw == null)
      return fw;

    return Float.valueOf(fw.get());
  }
}