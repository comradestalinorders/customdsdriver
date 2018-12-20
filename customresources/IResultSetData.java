package net.is_bg.ltf.config.customresources;

import java.util.List;

//this data structure must  correspond to the data structure returned by the webservice!!!

abstract interface IResultSetData{
  abstract List<String> getColumns();
  
  abstract List<Object[]> getResult();
  
  abstract Exception getException();
}