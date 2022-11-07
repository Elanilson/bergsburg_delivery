package com.bergburg.bergburgdelivery.listeners;

public interface OnListenerAcao<T> {
   void onClick(T obj);
   void onLongClick(T obj);
   void onClickObservacao(T obj);
}
