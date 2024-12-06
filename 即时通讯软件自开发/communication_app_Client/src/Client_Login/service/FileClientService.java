package Client_Login.service;

import app_common.Message;
import app_common.MessageType;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;

/**
 * 该类功能，用于实现文件传输服务( 接 / 收 )的相关代码
 */
public class FileClientService {
    /**
     *
     * @param src:要发送的文件地址
     * @param senderId:发送的用户的Id
     * @param getterId:接收的用户的Id
     */
    public void sendFileToOne(String src,String senderId,String getterId){
        //读取文件到message中
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        //创建文件输入流读取文件
        FileInputStream fileInputStream =null;
        byte[] fileBytes =new byte[(int)new File(src).length()];

        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes); //将src文件读入到程序的字节数组里
            //将文件对应的字节数组设置到message里
            message.setFileBytes(fileBytes);

//            if(fileBytes.length==0){
//                System.out.println("未读取到文件！");
//            }else{
//                System.out.println("读取到文件！");
//            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void saveFileOnGetter(Message message){
        System.out.println("\n"+message.getSender()+"给"+message.getGetter()+"发送文件："+message.getSrc());
        // 将弹窗操作放到 Platform.runLater 中,然后在弹窗中实现保存文件操作
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "用户: " + message.getSender(), ButtonType.OK);
            alert.setHeaderText("用户"+message.getSender()+"给你发来了一个文件！");
            //修改默认的按钮为保存按钮，点击后可以保存到本地的电脑上
            ButtonType okButtonType = ButtonType.OK;
            // 获取弹窗按钮实例
            Button okButton = (Button) alert.getDialogPane().lookupButton(okButtonType);

            if(okButton!=null){
                okButton.setText("确认接收并保存");
            }
            okButton.setOnAction(event->{
                System.out.println("用户"+message.getGetter()+"确认接收文件");
                //使用JavaFX中的DirectoryChooser选择器获得保存地址
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                // 打开目录选择对话框，返回选中的目录
                File selectedDirectory = directoryChooser.showDialog(new Stage());
                //如果选择的目录不为空，那么就保存到message中的dest里
                //BUG修复：不能正确保存文件！原因：通过DirectoryChooser得到的只是保存的目录，没有包括文件名
                //解决：在后面加入传来时的文件名字，拼接在一块使之变成合法的文件保存格式
                if(selectedDirectory!=null){
                    message.setDest(selectedDirectory.getAbsolutePath());  //获取绝对路径
                    System.out.println("选中的目录: " + message.getDest());  //检查结果
                    // 确保文件路径包含文件名
                    String fileName = message.getSrc().substring(message.getSrc().lastIndexOf(File.separator) + 1);
                    String filePath = message.getDest() + File.separator + fileName;
                    // 设置完整的文件路径
                    message.setDest(filePath);
                    System.out.println("最终的路径: " + message.getDest());  //检查结果
                    alert.close();
                    //取出message的文件字节数组，并通过文件输出流写出到磁盘上
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                        fileOutputStream.write(message.getFileBytes());
                        fileOutputStream.close();
                        System.out.println("\n 保存文件成功！");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    System.out.println("没有选择目录!");
                }
                alert.close();
            });
            // 显示弹窗并等待用户操作,不操作则不退出
            alert.showAndWait();
        });
    }
}
