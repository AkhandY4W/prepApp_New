package com.youth4work.prepapp.ui.workmail.manager;


import com.youth4work.prepapp.network.model.response.BusinessObject;

public class Interfaces {

    public interface IDataRetrievalListener{
        public void onDataRetrieved(BusinessObject businessObject);
    }

    public interface IDataRetrievalListenerString{
        public void onDataRetrieved(String string);
    }
}
