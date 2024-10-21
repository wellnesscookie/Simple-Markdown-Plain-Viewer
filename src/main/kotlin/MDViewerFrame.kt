package com.github.wellnesscookie

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import java.io.File
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

class MDViewerFrame : JFrame() {

    object Properties {
        const val FRAME_TITLE = "Markdown Plain Text Viewer"
        const val WIDTH_RATIO = 0.6  // Ratio of the app frame's width to screen width
        const val HEIGHT_RATIO = 0.9 // Ratio of the app frame's height to screen height
        const val BUTTON_LOAD_TEXT = "Import Markdown File"
        const val ERROR_INVALID_FILE_TYPE = "You need to select a Markdown file (.md or .markdown)"
        const val ERROR_TITLE = "Invalid File selected"
    }

    init {
        setupFrame()
    }

    private fun setupFrame() {
        title = Properties.FRAME_TITLE

        size = calculateFrameSize()
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)

        layout = BorderLayout()
        createUI()

        isVisible = true
    }

    private fun calculateFrameSize(): Dimension {
        val screenSize = Toolkit.getDefaultToolkit().screenSize

        // adjusts app window bounds relative to the max screen width and height
        val appWidth = (screenSize.width * Properties.WIDTH_RATIO).toInt()
        val appHeight = (screenSize.height * Properties.HEIGHT_RATIO).toInt()

        return Dimension(appWidth, appHeight)
    }

    private fun createUI() {
        val loadMDButton = JButton(Properties.BUTTON_LOAD_TEXT)
        val displayMDTextArea = JTextArea().apply {
            isEditable = false
            lineWrap = true
        }

        val scrollableMDViewer = JScrollPane(displayMDTextArea)

        loadMDButton.addActionListener {
            val selectedFile = showFileBrowser()
            if (selectedFile != null) {
                if (isValidMarkdownFile(selectedFile)) {
                    displayMDTextArea.text = readMarkdownFile(selectedFile)
                    displayMDTextArea.setCaretPosition(0) // force the text area to set scroll state on top
                } else {
                    showErrorDialog()
                }
            }
        }

        add(loadMDButton, BorderLayout.PAGE_START)
        add(scrollableMDViewer, BorderLayout.CENTER)
    }

    private fun showFileBrowser(): File? {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("Markdown Extensions", "md", "markdown")
        return if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile
        } else {
            null
        }
    }

    private fun isValidMarkdownFile(file: File): Boolean {
        return (file.extension == "md") or (file.extension == "markdown")
    }

    private fun showErrorDialog() {
        JOptionPane.showMessageDialog(
            this,
            Properties.ERROR_INVALID_FILE_TYPE,
            Properties.ERROR_TITLE,
            JOptionPane.ERROR_MESSAGE
        )
    }

    private fun readMarkdownFile(file: File): String {
        // retrieves raw text without parsing
        return file.readText()
    }
}
