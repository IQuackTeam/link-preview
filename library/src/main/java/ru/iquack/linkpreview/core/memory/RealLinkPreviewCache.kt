package ru.iquack.linkpreview.core.memory


/*** A simple in-memory cache implementation using LinkedHashMap.
 * * @param initialCapacity Initial capacity of the cache.
 * * @param loadFactor Load factor for the cache.
 * * @param accessOrder If true, the cache will be ordered by access order.
 * * @param maxCacheElements Maximum number of elements to keep in the cache.
 */
internal class RealLinkPreviewCache(
    initialCapacity: Int,
    loadFactor: Float,
    accessOrder: Boolean,
    maxCacheElements: Int,
) : LinkPreviewCache {

    private val cache = CacheMap(initialCapacity, loadFactor, accessOrder, maxCacheElements)

    override val keys: Set<CacheKey>
        get() = cache.keys

    override fun get(
        key: CacheKey
    ): CacheValue? = cache.get(key)

    override fun set(
        key: CacheKey,
        value: CacheValue
    ) = cache.set(key, value)

    override fun remove(key: CacheKey): Boolean{
        cache.remove(key)
        return cache.contains(key)
    }


    override fun clear() = cache.clear()

    private class CacheMap(
        initialCapacity: Int,
        loadFactor: Float,
        accessOrder: Boolean,
        private val maxCacheElements: Int,
    ): LinkedHashMap<CacheKey, CacheValue>(initialCapacity, loadFactor, accessOrder){

        override fun removeEldestEntry(eldest: Map.Entry<CacheKey, CacheValue>): Boolean {
            return size > maxCacheElements
        }

    }

}