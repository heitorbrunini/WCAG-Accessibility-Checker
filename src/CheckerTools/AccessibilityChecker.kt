import com.intellij.codeInspection.*
import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.css.CssLanguage
import com.intellij.psi.PsiFile
import javax.swing.JOptionPane

class AccessibilityChecker : LocalInspectionTool() {
    private val apiKey = "sua-chave-api"  // Substitua pela sua chave de API do OpenAI
    private val chatGPTClient = ChatGPTClient(apiKey)

    override fun getDisplayName() = "Accessibility Checker with ChatGPT"
    override fun getGroupDisplayName() = "Accessibility"
    override fun getShortName() = "AccessibilityChecker"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitFile(file: PsiFile) {
                if (file.language.isKindOf(HTMLLanguage.INSTANCE) || file.language.isKindOf(CssLanguage.INSTANCE)) {
                    val fileContent = file.text
                    chatGPTClient.evaluateAccessibility(fileContent) { feedback ->
                        displayAccessibilityIssues(feedback, holder)
                    }
                }
            }
        }
    }

    private fun displayAccessibilityIssues(feedback: String, holder: ProblemsHolder) {
        val categorizedIssues = parseFeedback(feedback)
        categorizedIssues.forEach { (category, issues) ->
            issues.forEach { issue ->
                holder.registerProblem(
                    issue.psiElement,
                    "$category: ${issue.message}",
                    ProblemHighlightType.WARNING,
                    AccessibilityQuickFix(issue.message)
                )
            }
        }
    }

    private fun parseFeedback(feedback: String): Map<String, List<AccessibilityIssue>> {
        // Parse o feedback recebido do ChatGPT e categorize conforme Perceptível, Operável, Compreensível, Robusto
        // Simulação de parse para este exemplo
        val issues = mutableMapOf<String, List<AccessibilityIssue>>()
        // Adicione aqui o código de parse que converte o feedback JSON em categorias e mensagens
        return issues
    }
}

data class AccessibilityIssue(val message: String, val psiElement: PsiElement)

class AccessibilityQuickFix(private val issueDescription: String) : LocalQuickFix {
    override fun getName() = "Ver Detalhes e Sugestões para '$issueDescription'"
    override fun getFamilyName() = "Accessibility Quick Fix"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        ProblemDetailsDialog(issueDescription).show()
    }
}
