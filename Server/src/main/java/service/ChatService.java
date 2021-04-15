package service;

public class ChatService extends BaseService
{
    /**
     * 发送私聊信息
     * @param senderId 发送方Id
     * @param receiverId 接收方Id
     * @param message 消息内容
     * @param token 用户token
     */
    public void sendMessageToUser(int senderId,int receiverId,String message,String token)
    {
        if(checkToken(senderId,token)!=OK)
        {
            return;
        }
    }

    /**
     * 发送群聊消息
     * @param senderId 发送方Id
     * @param groupId 群聊Id
     * @param message 消息内容
     * @param token 用户Token
     */
    public void sendMessageToGroup(int senderId,int groupId,String message,String token)
    {
        if(checkToken(senderId,token)!=OK)
        {
            return;
        }
    }
}
