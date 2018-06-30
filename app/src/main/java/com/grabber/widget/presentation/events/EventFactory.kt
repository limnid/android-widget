package com.grabber.widget.presentation.events

import android.os.Bundle

class EventFactory: EventBus {

    companion object {
        private val LOCK = Any()
        private var instance: EventBus? = null
        fun instance(): EventBus {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = EventFactory()
                }
                return instance!!
            }
        }
    }

    private var mEventObservables: MutableMap<String, EventObservable>? = mutableMapOf()

    override fun subscribe(key: String, observable: EventObservable) {
        mEventObservables?.put(key, observable)
    }

    override fun unsubscribe(key: String) {
        mEventObservables?.remove(key)
    }

    override fun emit(bundle: Bundle?) {
        mEventObservables?.forEach {
            it.value.onEvent(bundle)
        }
    }
}