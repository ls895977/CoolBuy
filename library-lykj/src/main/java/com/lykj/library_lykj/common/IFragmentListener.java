package com.lykj.library_lykj.common;

/**
 * 用于Fragment通信
 */
public interface IFragmentListener {
    void sendMsg(int flag, Object obj);
}
