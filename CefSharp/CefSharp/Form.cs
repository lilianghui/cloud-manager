using System.Configuration;
using System.IO;
using System.Windows.Forms;
using CefSharp.entity;
using CefSharp.events;
using CefSharp.hamdler;
using CefSharp.utils;
using CefSharp.WinForms;

namespace CefSharp
{
    public partial class Form : System.Windows.Forms.Form
    {

        Configuration configuration = ConfigurationManager.OpenExeConfiguration(ConfigurationUserLevel.None);


        public Form()
        {
            InitializeComponent();
            InitBrowser();
            this.WindowState = FormWindowState.Maximized;

            // 禁用最大化和最小化按钮
            // this.MaximizeBox = false;
            // this.MinimizeBox = false;
            // this.FormBorderStyle = FormBorderStyle.FixedSingle;
            // 这样的话，最大化、最小化按钮都会消失
            // this.FormBorderStyle = FormBorderStyle.Fixed3D; 

            new SerialObject(this);
        }

        public ChromiumWebBrowser browser;
        public void InitBrowser()
        {
            Config config = YamlSerializeObject.Deserializer<Config>(Directory.GetCurrentDirectory() + @"\config\config.yaml");
            string url = config.Url == null ? configuration.AppSettings.Settings["url"].Value : config.Url;

            CefSettings settings = new CefSettings();

            Cef.Initialize(settings);
            browser = new ChromiumWebBrowser(url);
            this.Controls.Add(browser);
            browser.Dock = DockStyle.Fill;
            browser.RegisterJsObject("instance", new InterfaceEvent(), false);
            browser.RequestHandler = new CefSharpContextHandler();
            browser.KeyboardHandler = new KeyBoardHander();
            browser.MenuHandler = new WebContextMenu();
            browser.DownloadHandler = new DownloadHandler();
            
        }


    }
    
}
