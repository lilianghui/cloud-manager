using System.Windows.Forms;

namespace CefSharp.hamdler
{
    class KeyBoardHander : IKeyboardHandler
    {
        public bool OnKeyEvent(IWebBrowser browserControl, IBrowser browser, KeyType type, int windowsKeyCode, int nativeKeyCode, CefEventFlags modifiers, bool isSystemKey)
        {
            return false;
        }

        public bool OnPreKeyEvent(IWebBrowser browserControl, IBrowser browser, KeyType type, int windowsKeyCode, int nativeKeyCode, CefEventFlags modifiers, bool isSystemKey, ref bool isKeyboardShortcut)
        {
            switch ((Keys)windowsKeyCode)
            {
                case Keys.F12:
                    browser.ShowDevTools();
                    break;
                case Keys.F5:
                    browser.Reload(); //此处可以添加想要实现的代码段
                    break;
            }
            return false;
        }
    }
}
