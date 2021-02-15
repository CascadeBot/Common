package org.cascadebot.common

import java.lang.reflect.InaccessibleObjectException

class ClassDiffBuilder<T : Any> {

    val diffs: List<Diff<Any>> = listOf()

    fun createFromObjects(lhs: T, rhs: T, path: String = "") {
        for (field in lhs.javaClass.declaredFields) {
            if (!field.canAccess(lhs) || !field.canAccess(rhs)) {
                try {
                    field.isAccessible = true
                } catch (e: InaccessibleObjectException) {
                    continue
                } catch (e: SecurityException) {
                    continue
                }
            }

        }
    }

    private fun <E : Any> getDiff(path: String, lhs: E, rhs: E): Diff<*>? {
        if (lhs is List<*> && rhs is List<*>) {
            return ListDiff(path, lhs as List<Any>, rhs as List<Any>)
        } else if (lhs is Set<*> && rhs is Set<*>) {
            return SetDiff(path, lhs as Set<Any>, rhs as Set<Any>)
        }

        // More types:
        // - Normal POJO objects
        // - Primitives
        // - etc...

        return null;
    }

}

sealed class Diff<T : Any>(val path: String, val lhs: T, val rhs: T)

class ListDiff<T : List<Any>>(path: String, lhs: T, rhs: T) : Diff<T>(path, lhs, rhs) {

    val added: List<Any> = rhs.filterNot { lhs.contains(it) };
    val removed: List<Any> = lhs.filterNot { rhs.contains(it) }
    val unchanged: List<Any> = lhs.filter { rhs.contains(it) }

}

class SetDiff<I : Any>(path: String, lhs: Set<I>, rhs: Set<I>) : Diff<Set<I>>(path, lhs, rhs) {

    val added: List<I> = rhs.filterNot { lhs.contains(it) };
    val removed: List<I> = lhs.filterNot { rhs.contains(it) }
    val unchanged: List<I> = lhs.filter { rhs.contains(it) }

}