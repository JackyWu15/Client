// IBookListenerlInterface.aidl
package com.hechuangwu.server.aidl ;
import com.hechuangwu.server.aidl.Book;
// Declare any non-default types here with import statements

interface IBookListenerlInterface {
   void addNewBookListener(in Book book);
}
