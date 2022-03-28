package com.nightgame.leeframework.utils;

import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

public class MsgUtils
{
    private static String _PlatformObj = "GameMgr";//Unity接收对象的名字
    private static String _PlatMethod = "OnMessage";//接收对象中的方法名

    public static void sendMsgToUnity(int msgId, String msg)
    {
        Utils.log("sendMsgToUnity : " + msg);
        JSONObject rspMsg = new JSONObject();
        try
        {
            rspMsg.put("Id", msgId);
            rspMsg.put("Json", msg);
            UnityPlayer.UnitySendMessage(_PlatformObj, _PlatMethod, rspMsg.toString());
        }
        catch (JSONException ex)
        {
            Utils.log("Json Fail : " + ex.toString());
        }
    }
}
