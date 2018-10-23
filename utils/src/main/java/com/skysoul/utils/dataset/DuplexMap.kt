package com.skysoul.utils.dataset

import java.util.*

/**
 * 双向map，用户有限，要求key和value都是唯一的，而且不能为空，方便根据value去找到key
 */

class DuplexMap<K, V> {

    inner class Entry(var k: K, var v: V) {

    }

    private var kEntryMap: HashMap<K, Entry> = HashMap()
    private var vEntryMap: HashMap<V, Entry> = HashMap()

    public fun contains(k: K): Boolean {
        return kEntryMap.containsKey(k)
    }

    fun containsValue(v: V): Boolean {
        return vEntryMap.containsKey(v)
    }

    fun getVaule(k: K): V? {
        var e = kEntryMap.get(k)
        return e?.v
    }

    fun getKey(v: V): K? {
        return vEntryMap.get(v)?.k
    }

    fun removeByKey(k: K): V? {
        var e = kEntryMap.remove(k)
        if (e != null)
            vEntryMap.remove(e.v)
        return e?.v
    }

    fun removeByValue(v:V):K?{
        var e = vEntryMap.remove(v)
        if(e!=null)
            kEntryMap.remove(e.k)
        return e?.k
    }

    fun put(k: K, v: V) {
        var e: Entry = Entry(k, v)
        if (contains(k)) {
            removeByKey(k)
        }
        if(containsValue(v))
            removeByValue(v)

        kEntryMap.put(k,e)
        vEntryMap.put(v,e)
    }

}