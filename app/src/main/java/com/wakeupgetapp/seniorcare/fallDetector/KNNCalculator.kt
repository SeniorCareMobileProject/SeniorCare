package com.wakeupgetapp.seniorcare.fallDetector

import org.apache.commons.collections4.queue.CircularFifoQueue
import kotlin.math.abs

class KNNCalculator {
    val FALL_THRESHOLD = 100
    private var QUEUE_LIMIT = 100
    private var N_NEIGHBORS = 5
    private var WINDOW_SIZE = 4
    private val limitedQueue: CircularFifoQueue<AccelerometerData> = CircularFifoQueue(QUEUE_LIMIT)

    fun newData(coordinates: AccelerometerData): Double {
        limitedQueue.add(coordinates)
        return if (limitedQueue.size == QUEUE_LIMIT) {
            calculateKNN()
        } else {
            return 0.0
        }
    }

    private fun calculateKNN(): Double {
        val distances: MutableList<Double> = ArrayList()
        var x = 0.0
        var y = 0.0
        var z = 0.0
        var element: AccelerometerData
        val iterator: Iterator<*> = limitedQueue.iterator()
        for (i in 0 until WINDOW_SIZE) {
            element = iterator.next() as AccelerometerData
            x += element.xCoordinate
            y += element.yCoordinate
            z += element.zCoordinate
        }
        val newElement = Triple(x, y, z)
        var i = WINDOW_SIZE
        while (i < QUEUE_LIMIT) {
            x = 0.0
            y = 0.0
            z = 0.0
            for (j in 0 until WINDOW_SIZE) {
                element = iterator.next() as AccelerometerData
                x += element.xCoordinate
                y += element.yCoordinate
                z += element.zCoordinate
            }
            distances.add(countDistanceVertical(newElement, Triple(x, y, z)))
            i += WINDOW_SIZE
        }
        return distances.stream()
            .sorted()
            .limit(N_NEIGHBORS.toLong())
            .mapToDouble { obj: Double -> obj }
            .sum()
    }

    private fun countDistanceVertical(
        newElement: Triple<Double, Double, Double>,
        elem: Triple<Double, Double, Double>
    ): Double {
        return abs(newElement.second - elem.second)
    }
}