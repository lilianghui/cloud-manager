using System;
using System.IO.Ports;
using System.Text;
using System.Windows.Forms;

namespace CefSharp.utils
{
    class SerialObject
    {
        private SerialPort sp1 = new SerialPort();
        private Form form;

        public SerialObject(Form form)
        {
            this.form = form;

            string[]  str = SerialPort.GetPortNames();
            if (str == null)
            {
                MessageBox.Show("本机没有串口！", "Error");
                return;
            }
            sp1.PortName = "COM2";
            sp1.DtrEnable = true;//获取或设置一个值，该值在串行通信过程中启用数据终端就绪 (DTR) 信号。
            sp1.RtsEnable = true;//获取或设置一个值，该值指示在串行通信中是否启用请求发送 (RTS) 信号
            sp1.DataReceived += Sp1_DataReceived;
            sp1.BaudRate = 9600;       //波特率
            sp1.ReadTimeout = 1000;//设置数据读取超时为1秒
            sp1.DataBits = 8;       //数据位
            sp1.Open();     //打开串口
            MessageBox.Show("打开串口"+ str[0], "Error");
            Console.Write("打开串口:{0}", str[0]);

        }
        ~ SerialObject()
        {
            if (sp1.IsOpen)
            {
                sp1.Close();
            }
        }

        private void Sp1_DataReceived(object sender, SerialDataReceivedEventArgs e)
        {
            if (sp1.IsOpen)     //判断是否打开串口
            {
                //输出当前时间
                try
                {
                    Byte[] receivedData = new Byte[sp1.BytesToRead];        //创建接收字节数组
                    sp1.Read(receivedData, 0, receivedData.Length);         //读取数据
                    string code=(new UTF8Encoding().GetString(receivedData));//用万能的UTF8可以传输中文不会乱码
                    code = System.Text.Encoding.Default.GetString(receivedData);
                    this.form.browser.ExecuteScriptAsync("barCode", code);
                }
                catch (System.Exception ex)
                {
                    MessageBox.Show(ex.Message, "出错提示!!!!!");
                }
            }
            else
            {
                MessageBox.Show("请打开某个串口", "错误提示");
            }
        }

    }


}
