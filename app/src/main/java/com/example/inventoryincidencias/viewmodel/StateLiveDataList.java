package com.example.inventoryincidencias.viewmodel;

import androidx.lifecycle.MutableLiveData;

/**
 * En esta clase se crearán los métodos set que podrá usar la clase ViewModel
 */
public class StateLiveDataList<T> extends MutableLiveData<StateDataList<T>> {

    //private StateDataList<T> stateDataList = new StateDataList<T>();

    public void setLoading() {
        setValue(new StateDataList<T>().loading());
        //setValue(stateDataList.loading());
    }

    public void setNoData() {
        setValue(new StateDataList<T>().nodata());
    }

    public void setSuccess(T data) {
        setValue(new StateDataList<T>().success(data));
    }

    public void setComplete() {
        setValue(new StateDataList<T>().complete());
    }

    public void setOrderById(T data) {
        setValue(new StateDataList<T>().orderById(data));
    }
}
