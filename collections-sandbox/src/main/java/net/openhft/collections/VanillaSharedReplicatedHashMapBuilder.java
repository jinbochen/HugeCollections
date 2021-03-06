/*
 * Copyright 2014 Higher Frequency Trading 
 * <p/>
 * http://www.higherfrequencytrading.com 
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Rob Austin.
 */
public class VanillaSharedReplicatedHashMapBuilder extends SharedHashMapBuilder implements Cloneable {

    public static final int META_BYTES_SIZE = 16;
    private byte identifier = 1;

    boolean canReplicate;

    public boolean canReplicate() {
        return canReplicate;
    }

    public VanillaSharedReplicatedHashMapBuilder canReplicate(boolean canReplicate) {
        this.canReplicate = canReplicate;
        return this;
    }

    int alignedEntrySize() {
        return entryAndValueAlignment().alignSize(entrySize() + META_BYTES_SIZE);
    }

    public <K, V> VanillaSharedReplicatedHashMap<K, V> create(File file, Class<K> kClass, Class<V> vClass) throws IOException {
        VanillaSharedReplicatedHashMapBuilder builder = clone();

        for (int i = 0; i < 10; i++) {
            if (file.exists() && file.length() > 0) {
                readFile(file, builder);
                break;
            }
            if (file.createNewFile() || file.length() == 0) {
                newFile(file);
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
        }
        if (builder == null || !file.exists())
            throw new FileNotFoundException("Unable to create " + file);
        return new VanillaSharedReplicatedHashMap<K, V>(builder, file, kClass, vClass);
    }


    @Override
    public VanillaSharedReplicatedHashMapBuilder clone() {
        return (VanillaSharedReplicatedHashMapBuilder) super.clone();
    }

    /**
     * Set minimum number of segments.
     * See concurrencyLevel in {@link java.util.concurrent.ConcurrentHashMap}.
     *
     * @return this builder object back
     */
    public VanillaSharedReplicatedHashMapBuilder minSegments(int minSegments) {
        super.minSegments(minSegments);
        return this;
    }


    /**
     * <p>Note that the actual entrySize will be aligned
     * to 4 (default entry alignment). I. e. if you set entry size to 30, the
     * actual entry size will be 32 (30 aligned to 4 bytes). If you don't want
     * entry size to be aligned, set
     * {@code entryAndValueAlignment(Alignment.NO_ALIGNMENT)}.
     *
     * @param entrySize the size in bytes
     * @return this {@code VanillaSharedReplicatedHashMapBuilder} back
     * @see #entryAndValueAlignment(Alignment)
     * @see #entryAndValueAlignment()
     */
    public VanillaSharedReplicatedHashMapBuilder entrySize(int entrySize) {
        super.entrySize(entrySize);
        return this;
    }


    /**
     * Specifies alignment of address in memory of entries
     * and independently of address in memory of values within entries.
     * <p/>
     * Useful when values of the map are updated intensively, particularly
     * fields with volatile access, because it doesn't work well
     * if the value crosses cache lines. Also, on some (nowadays rare)
     * architectures any misaligned memory access is more expensive than aligned.
     * <p/>
     * Note that specified {@link #entrySize()} will be aligned according to
     * this alignment. I. e. if you set {@code entrySize(20)} and
     * {@link net.openhft.collections.Alignment#OF_8_BYTES}, actual entry size
     * will be 24 (20 aligned to 8 bytes).
     *
     * @return this {@code VanillaSharedReplicatedHashMapBuilder} back
     * @see #entryAndValueAlignment()
     */
    public VanillaSharedReplicatedHashMapBuilder entryAndValueAlignment(Alignment alignment) {
        super.entryAndValueAlignment(alignment);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder entries(long entries) {
        super.entries(entries);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder replicas(int replicas) {
        super.replicas(replicas);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder actualEntriesPerSegment(int actualEntriesPerSegment) {
        super.actualEntriesPerSegment(actualEntriesPerSegment);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder actualSegments(int actualSegments) {
        super.actualSegments(actualSegments);
        return this;
    }

    /**
     * Not supported yet.
     *
     * @return an instance of the map builder
     */
    public VanillaSharedReplicatedHashMapBuilder transactional(boolean transactional) {
        super.transactional(transactional);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder lockTimeOutMS(long lockTimeOutMS) {
        super.lockTimeOutMS(lockTimeOutMS);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder errorListener(SharedMapErrorListener errorListener) {
        super.errorListener(errorListener);
        return this;
    }


    /**
     * Map.put() returns the previous value, functionality which is rarely used but fairly cheap for HashMap.
     * In the case, for an off heap collection, it has to create a new object (or return a recycled one)
     * Either way it's expensive for something you probably don't use.
     *
     * @param putReturnsNull false if you want SharedHashMap.put() to not return the object that was replaced but instead return null
     * @return an instance of the map builder
     */
    public VanillaSharedReplicatedHashMapBuilder putReturnsNull(boolean putReturnsNull) {
        super.putReturnsNull(putReturnsNull);
        return this;
    }


    /**
     * Map.remove()  returns the previous value, functionality which is rarely used but fairly cheap for HashMap.
     * In the case, for an off heap collection, it has to create a new object (or return a recycled one)
     * Either way it's expensive for something you probably don't use.
     *
     * @param removeReturnsNull false if you want SharedHashMap.remove() to not return the object that was removed but instead return null
     * @return an instance of the map builder
     */
    public VanillaSharedReplicatedHashMapBuilder removeReturnsNull(boolean removeReturnsNull) {
        super.removeReturnsNull(removeReturnsNull);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder generatedKeyType(boolean generatedKeyType) {
        super.generatedKeyType(generatedKeyType);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder generatedValueType(boolean generatedValueType) {
        super.generatedValueType(generatedValueType);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder largeSegments(boolean largeSegments) {
        super.largeSegments(largeSegments);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder metaDataBytes(int metaDataBytes) {
        super.metaDataBytes(metaDataBytes);
        return this;
    }


    public VanillaSharedReplicatedHashMapBuilder eventListener(SharedMapEventListener eventListener) {
        super.eventListener(eventListener);
        return this;
    }

    private TimeProvider timeProvider = new TimeProvider();

    public VanillaSharedReplicatedHashMapBuilder timeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        return this;
    }

    public TimeProvider timeProvider() {
        return timeProvider;
    }

    public byte identifier() {
        return identifier;
    }

    public void identifier(byte identifier) {
        this.identifier = identifier;
    }

}
