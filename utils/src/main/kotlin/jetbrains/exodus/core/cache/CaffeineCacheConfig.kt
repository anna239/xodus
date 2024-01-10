/*
 * Copyright ${inceptionYear} - ${year} ${owner}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrains.exodus.core.cache

import java.time.Duration


data class CaffeineCacheConfig<K, V>(
    val sizeEviction: SizeEviction<K, V>,
    val expireAfterAccess: Duration? = null,
    val useSoftValues: Boolean = true,
    /**
     * Used for testing purposes only.
     *
     * This provides more predictable response times by not penalizing the caller and uses ForkJoinPool.commonPool() by default.
     * Uses the Caffeine.executor(Executor) method to specify a direct (same thread) executor in your cache builder,
     * rather than having to wait for the asynchronous tasks to complete.
     */
    val directExecution: Boolean = false
)

sealed interface SizeEviction<K, V> {
    val size: Long
}

data class FixedSizeEviction<K, V>(val maxSize: Long) : SizeEviction<K, V> {
    override val size: Long = maxSize
}

data class WeightSizeEviction<K, V>(
    val maxWeight: Long = -1,
    val weigher: (K, V) -> Int
) : SizeEviction<K, V> {
    override val size: Long = maxWeight
}