import com.intellij.openapi.options.Configurable
import javax.swing.*
import java.awt.GridLayout

class AccessibilityCheckerConfigurable : Configurable {
    private val settings = AccessibilityCheckerSettings.getInstance().state

    private val temperatureField = JTextField(settings.temperature.toString())
    private val htmlVersionField = JComboBox(arrayOf("HTML5", "HTML4", "XHTML"))
    private val cssVersionField = JComboBox(arrayOf("CSS3", "CSS2"))
    private val apiKeyField = JTextField(settings.apiKey)
    private val oauthTokenField = JTextField(settings.oauthToken)

    init {
        htmlVersionField.selectedItem = settings.htmlVersion
        cssVersionField.selectedItem = settings.cssVersion
    }

    override fun createComponent(): JComponent {
        val panel = JPanel(GridLayout(5, 2, 10, 10))

        panel.add(JLabel("Temperature (0.0 - 1.0):"))
        panel.add(temperatureField)

        panel.add(JLabel("HTML Version:"))
        panel.add(htmlVersionField)

        panel.add(JLabel("CSS Version:"))
        panel.add(cssVersionField)

        panel.add(JLabel("API Key:"))
        panel.add(apiKeyField)

        panel.add(JLabel("OAuth Token:"))
        panel.add(oauthTokenField)

        return panel
    }

    override fun isModified(): Boolean {
        return temperatureField.text.toDouble() != settings.temperature ||
                htmlVersionField.selectedItem != settings.htmlVersion ||
                cssVersionField.selectedItem != settings.cssVersion ||
                apiKeyField.text != settings.apiKey ||
                oauthTokenField.text != settings.oauthToken
    }

    override fun apply() {
        settings.temperature = temperatureField.text.toDouble()
        settings.htmlVersion = htmlVersionField.selectedItem as String
        settings.cssVersion = cssVersionField.selectedItem as String
        settings.apiKey = apiKeyField.text
        settings.oauthToken = oauthTokenField.text
    }

    override fun reset() {
        temperatureField.text = settings.temperature.toString()
        htmlVersionField.selectedItem = settings.htmlVersion
        cssVersionField.selectedItem = settings.cssVersion
        apiKeyField.text = settings.apiKey
        oauthTokenField.text = settings.oauthToken
    }

    override fun getDisplayName(): String {
        return "Accessibility Checker Settings"
    }
}
