import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "AccessibilityCheckerSettings", storages = [Storage("accessibilityCheckerSettings.xml")])
@Service
class AccessibilityCheckerSettings : PersistentStateComponent<AccessibilityCheckerSettings.State> {
    var state = State()

    data class State(
        var temperature: Double = 0.7,
        var htmlVersion: String = "HTML5",
        var cssVersion: String = "CSS3",
        var apiKey: String = "",
        var oauthToken: String = ""
    )

    override fun getState(): State {
        return state
    }

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): AccessibilityCheckerSettings {
            return ServiceManager.getService(AccessibilityCheckerSettings::class.java)
        }
    }
}
