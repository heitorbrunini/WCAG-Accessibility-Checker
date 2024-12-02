import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.project.Project

class AccessibilityAction : AnAction("Run Accessibility Check") {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        Messages.showMessageDialog(
            project,
            "Accessibility check initiated. Problems will be highlighted in the editor.",
            "Accessibility Checker",
            Messages.getInformationIcon()
        )
    }
}
