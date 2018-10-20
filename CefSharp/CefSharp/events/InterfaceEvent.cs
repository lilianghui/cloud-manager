using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CefSharp.events
{
    class InterfaceEvent
    {
        public string MessageText = string.Empty;

        public void ShowTest()
        {
            MessageBox.Show("this in C#.\n\r" + MessageText);
        }
    }
}
