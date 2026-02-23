using System;
using System.IO;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace redactor
{
    public partial class Form1 : Form
    {
        private string currentFilePath = "";

        public Form1()
        {
            InitializeComponent();
        }
        private void Form1_Load(object sender, EventArgs e)
        {
           
        }
        private void NewFile_Click(object sender, EventArgs e)
        {
            textBox1.Clear();
            currentFilePath = "";
        }

        private void OpenFile_Click(object sender, EventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.Filter = "Text files (*.txt)|*.txt|All files (*.*)|*.*";

            if (ofd.ShowDialog() == DialogResult.OK)
            {
                currentFilePath = ofd.FileName;
                textBox1.Text = File.ReadAllText(currentFilePath);
            }
        }

        private void SaveFile_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(currentFilePath))
            {
                SaveFileDialog sfd = new SaveFileDialog();
                sfd.Filter = "Text files (*.txt)|*.txt";

                if (sfd.ShowDialog() == DialogResult.OK)
                    currentFilePath = sfd.FileName;
            }

            if (!string.IsNullOrEmpty(currentFilePath))
                File.WriteAllText(currentFilePath, textBox1.Text);
        }

        private void Exit_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }

}
