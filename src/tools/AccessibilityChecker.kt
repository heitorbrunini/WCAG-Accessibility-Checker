import com.intellij.codeInspection.*
import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.css.CssLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.openapi.editor.EditorFactory
import org.jetbrains.kotlin.idea.util.projectStructure.module

class AccessibilityChecker : LocalInspectionTool() {

    override fun getDisplayName() = "Accessibility Checker for HTML and CSS"
    override fun getGroupDisplayName() = "Accessibility"
    override fun getShortName() = "AccessibilityChecker"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitFile(file: PsiFile) {
                when {
                    file.language.isKindOf(HTMLLanguage.INSTANCE) -> checkHtmlAccessibility(file, holder)
                    file.language.isKindOf(CssLanguage.INSTANCE) -> checkCssAccessibility(file, holder)
                }
            }
        }
    }

    private fun checkHtmlAccessibility(file: PsiFile, holder: ProblemsHolder) {
        val imgTags = PsiTreeUtil.collectElements(file) { it.text.contains("<img") }
        imgTags.forEach { img ->
            if (!img.text.contains("alt=")) {
                holder.registerProblem(img, "Image tags should have an 'alt' attribute for accessibility.")
            }
        }
    }

    private fun checkCssAccessibility(file: PsiFile, holder: ProblemsHolder) {
        val colorElements = PsiTreeUtil.collectElements(file) { it.text.contains("color:") }
        colorElements.forEach { colorElement ->
            val colorValue = extractColorValue(colorElement.text)
            if (colorValue != null && !isAccessibleColor(colorValue)) {
                holder.registerProblem(colorElement, "Color contrast may not meet WCAG guidelines.")
            }
        }
    }

    private fun extractColorValue(text: String): String? {
        val regex = Regex("#[0-9a-fA-F]{6}")
        return regex.find(text)?.value
    }

    private fun isAccessibleColor(color: String): Boolean {
        // Simplified check, use a real contrast ratio library in production
        return color != "#ffffff" && color != "#000000"
    }
}
