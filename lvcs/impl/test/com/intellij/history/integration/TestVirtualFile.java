package com.intellij.history.integration;

import com.intellij.history.core.Paths;
import com.intellij.mock.MockLocalFileSystem;
import com.intellij.openapi.vfs.DeprecatedVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestVirtualFile extends DeprecatedVirtualFile {
  private String myName;
  private String myContent;
  private boolean isReadOnly;
  private long myTimestamp;

  private boolean IsDirectory;
  private VirtualFile myParent;
  private List<TestVirtualFile> myChildren = new ArrayList<TestVirtualFile>();

  public TestVirtualFile(@NotNull String name, String content, long timestamp) {
    this(name, content,  timestamp, false);
  }

  public TestVirtualFile(@NotNull String name, String content, long timestamp, boolean isReadOnly) {
    myName = name;
    myContent = content;
    this.isReadOnly = isReadOnly;
    myTimestamp = timestamp;
    IsDirectory = false;
  }

  public TestVirtualFile(String name) {
    myName = name;
    IsDirectory = true;
  }

  @NotNull
  public String getName() {
    return myName;
  }

  @Override
  protected boolean nameEquals(@NotNull String name) {
    return Paths.isCaseSensitive() ? myName.equals(name) : myName.equalsIgnoreCase(name);
  }

  public boolean isDirectory() {
    return IsDirectory;
  }

  public String getPath() {
    if (myParent == null) return myName;
    return myParent.getPath() + "/" + myName;
  }

  public long getTimeStamp() {
    return myTimestamp;
  }

  public VirtualFile[] getChildren() {
    return myChildren.toArray(new VirtualFile[0]);
  }

  public void addChild(TestVirtualFile f) {
    f.myParent = this;
    myChildren.add(f);
  }

  public long getLength() {
    return myContent == null ? 0 : myContent.getBytes().length;
  }

  public byte[] contentsToByteArray() throws IOException {
    return myContent == null ? null : myContent.getBytes();
  }

  @NotNull
  public VirtualFileSystem getFileSystem() {
    return new MockLocalFileSystem() {
      @Override
      public boolean equals(Object o) {
        return true;
      }
    };
  }

  public boolean isWritable() {
    return !isReadOnly;
  }

  public boolean isValid() {
    return true;
  }

  @Nullable
  public VirtualFile getParent() {
    return myParent;
  }

  public OutputStream getOutputStream(Object requestor, long newModificationStamp, long newTimeStamp) throws IOException {
    throw new UnsupportedOperationException();
  }

  public void refresh(boolean asynchronous, boolean recursive, Runnable postRunnable) {
    throw new UnsupportedOperationException();
  }

  public InputStream getInputStream() throws IOException {
    throw new UnsupportedOperationException();
  }
}
