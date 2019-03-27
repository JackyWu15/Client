// IBookAidlInterface.aidl
package com.hechuangwu.server.aidl ;
import com.hechuangwu.server.aidl.Book;
import com.hechuangwu.server.aidl.IBookListenerlInterface;
// Declare any non-default types here with import statements

interface IBookAidlInterface {
   void addBook(in Book book);
   List<Book>getBookList();
   void registerBookListener(in IBookListenerlInterface iBookListenerlInterface);
   void unRegisterBookListener(in IBookListenerlInterface iBookListenerlInterface);

}
