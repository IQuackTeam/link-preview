package ru.iquack.linkpreview.core.memory

import ru.iquack.linkpreview.core.LinkPreview

typealias CacheKey = String
typealias CacheValue = LinkPreview

interface LinkPreviewCache {

    val keys: Set<CacheKey>

    operator fun get(key: CacheKey): CacheValue?

    operator fun set(key: CacheKey, value: CacheValue)

    fun remove(key: CacheKey): Boolean

    fun clear()

    class Builder {
        private var initialCapacity: Int = 16
        private var loadFactor: Float = 0.75f
        private var accessOrder: Boolean = true
        private var maxCacheElements: Int = 100

        /**
         * Set the initial capacity of the cache.
         *
         * @param size Initial capacity.
         * @return The builder instance.
         */
        fun initialCapacity(size: Int) = apply {
            this.initialCapacity = size
        }

        /**
         * Set the load factor of the cache.
         *
         * @param factor Load factor.
         * @return The builder instance.
         */
        fun loadFactor(factor: Float) = apply {
            this.loadFactor = factor
        }

        /**
         * Set whether the cache should be ordered by access order.
         *
         * @param accessOrder True for access order, false for insertion order.
         * @return The builder instance.
         */
        fun accessOrder(accessOrder: Boolean) = apply {
            this.accessOrder = accessOrder
        }

        /**
         * Set the maximum number of elements in the cache.
         *
         * @param elements Maximum number of elements.
         * @return The builder instance.
         */
        fun maxCacheElements(elements: Int) = apply {
            this.maxCacheElements = elements
        }

        /**
         * Create a new [LinkPreviewCache] instance.
         */
        fun build(): LinkPreviewCache {
            return RealLinkPreviewCache(
                initialCapacity = initialCapacity,
                loadFactor = loadFactor,
                accessOrder = accessOrder,
                maxCacheElements = maxCacheElements
            )
        }
    }

    companion object{
        private val instance: LinkPreviewCache by lazy{ Builder().build() }
        fun getDefault(): LinkPreviewCache = instance
    }

}