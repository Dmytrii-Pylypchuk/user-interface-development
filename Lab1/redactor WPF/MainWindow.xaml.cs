using Microsoft.Win32;
using System.IO;
using System.Windows;

namespace redactor_WPF
{
    public partial class MainWindow : Window
    {
        private string currentFilePath = "";

        public MainWindow()
        {
            InitializeComponent();
        }

        private void NewFile_Click(object sender, RoutedEventArgs e)
        {
            Editor.Clear();
            currentFilePath = "";
        }

        private void OpenFile_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.Filter = "Text files (*.txt)|*.txt";

            if (ofd.ShowDialog() == true)
            {
                currentFilePath = ofd.FileName;
                Editor.Text = File.ReadAllText(currentFilePath);
            }
        }

        private void SaveFile_Click(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrEmpty(currentFilePath))
            {
                SaveFileDialog sfd = new SaveFileDialog();
                sfd.Filter = "Text files (*.txt)|*.txt";

                if (sfd.ShowDialog() == true)
                    currentFilePath = sfd.FileName;
            }

            if (!string.IsNullOrEmpty(currentFilePath))
                File.WriteAllText(currentFilePath, Editor.Text);
        }

        private void Exit_Click(object sender, RoutedEventArgs e)
        {
            Application.Current.Shutdown();
        }
    }
}