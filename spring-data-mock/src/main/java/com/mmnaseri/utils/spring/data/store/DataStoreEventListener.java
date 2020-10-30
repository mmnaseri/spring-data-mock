package com.mmnaseri.utils.spring.data.store;

/**
 * This interface encapsulates the task of listening to and reacting to an event
 *
 * @param <E> the type of the event to which this listener subscribes
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public interface DataStoreEventListener<E extends DataStoreEvent> {

  /**
   * Will be called by the data store when a relevant event happens
   *
   * @param event the event that has taken place
   */
  void onEvent(E event);
}
