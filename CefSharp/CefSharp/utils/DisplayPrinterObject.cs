using System;
using System.Collections.Generic;
using System.Drawing.Printing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CefSharp.utils
{
    class DisplayPrinterObject
    {
        public DisplayPrinterObject()
        {
            PrintDocument print = new PrintDocument();
            string sDefault = print.PrinterSettings.PrinterName;//默认打印机名
            MessageBox.Show(sDefault, "默认打印机!!!!!");
            foreach (string sPrint in PrinterSettings.InstalledPrinters)//获取所有打印机名称
            {

            }
        }
    }
}
