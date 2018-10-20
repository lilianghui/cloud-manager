using CefSharp.WinForms;
using System.Drawing;
using System.Windows.Forms;

namespace CefSharp.hamdler
{
    class WebContextMenu : IContextMenuHandler
    {
        public void OnBeforeContextMenu(IWebBrowser browserControl, IBrowser browser, IFrame frame, IContextMenuParams parameters, IMenuModel model)
        {
        }

        public bool OnContextMenuCommand(IWebBrowser browserControl, IBrowser browser, IFrame frame, IContextMenuParams parameters, CefMenuCommand commandId, CefEventFlags eventFlags)
        {
            return true;
        }

        public void OnContextMenuDismissed(IWebBrowser browserControl, IBrowser browser, IFrame frame)
        {
            //隐藏菜单栏
            var chromiumWebBrowser = (ChromiumWebBrowser)browserControl;
            //chromiumWebBrowser.ContextMenu = null;
        }

        public bool RunContextMenu(IWebBrowser browserControl, IBrowser browser, IFrame frame, IContextMenuParams parameters, IMenuModel model, IRunContextMenuCallback callback)
        {
            ChromiumWebBrowser chromiumWebBrowser = (ChromiumWebBrowser)browserControl;
            //绘制了一遍菜单栏  所以初始化的时候不必绘制菜单栏，再此处绘制即可
            var menu = new ContextMenu();
            menu.MenuItems.Add(new MenuItem("aaaaaaaaaa"));
            chromiumWebBrowser.ContextMenu = menu;
            return true;
        }
    }
}
