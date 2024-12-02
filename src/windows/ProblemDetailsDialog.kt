import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane
import java.awt.BorderLayout

class ProblemDetailsDialog(issueDescription: String) : DialogWrapper(true) {
    private val detailsArea = JTextArea()

    init {
        title = "Detalhes do Problema de Acessibilidade"
        detailsArea.text = "Descrição do problema:\n$issueDescription\n\nSugestão de Correção:\n${
            provideFixSuggestions(issueDescription)
        }"
        detailsArea.isEditable = false
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        panel.add(JLabel("Detalhes e Sugestões de Correção"), BorderLayout.NORTH)
        panel.add(JScrollPane(detailsArea), BorderLayout.CENTER)
        return panel
    }

    private fun provideFixSuggestions(issueDescription: String): String {
        // Aqui, podemos fornecer sugestões detalhadas baseadas no problema
        return when {
            "contraste de cores" in issueDescription.toLowerCase() -> "Considere aumentar o contraste de cores para melhorar a legibilidade."
            "texto alternativo" in issueDescription.toLowerCase() -> "Adicione um atributo 'alt' descritivo para a imagem."
            else -> "Consulte a documentação da WCAG 2.2 para mais detalhes."
        }
    }
}
