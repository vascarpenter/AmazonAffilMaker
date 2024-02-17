import com.amazon.paapi5.v1.*
import com.amazon.paapi5.v1.api.DefaultApi
import java.awt.Font
import java.util.*
import javax.swing.JFrame
import javax.swing.UIManager

fun main() {
    val f = JFrame("Amazon Affilliate Maker")
    val g = gui()
    UIManager.put("OptionPane.messageFont", Font("Dialog", Font.PLAIN, 12))
    UIManager.put("OptionPane.buttonFont", Font("Dialog", Font.PLAIN, 12))

    f.apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        contentPane = g.panel
        setSize(600, 500)
        isResizable = false
        setLocationRelativeTo(null)
        isVisible = true
    }

    g.createHTMLButton.addActionListener {
        val asin = g.asinField.text
        val txt = requestHTML(asin)
        g.htmlText.text = txt
    }
}

fun requestHTML( asin: String ) : String
{

    var txt = ""
    val client = ApiClient()
    client.accessKey = System.getenv("PA_ACCESS_KEY")
    client.secretKey = System.getenv("PA_SECRET_KEY")

    val partnerTag = System.getenv("PA_ASSOCIATE_TAG")

    client.host = "webservices.amazon.co.jp"
    client.region = "us-west-2"

    val api = DefaultApi(client)

    val searchItemsResources: MutableList<SearchItemsResource> = ArrayList()
    searchItemsResources.add(SearchItemsResource.IMAGES_PRIMARY_SMALL)
    searchItemsResources.add(SearchItemsResource.ITEMINFO_TITLE)
    val searchIndex = "All"

    // Specify keywords
    val keywords = asin // "B018WNIBJS"
    val searchItemsRequest = SearchItemsRequest().partnerTag(partnerTag).keywords(keywords)
        .searchIndex(searchIndex).resources(searchItemsResources).partnerType(PartnerType.ASSOCIATES)

    try {
        // Forming the request
        val response = api.searchItems(searchItemsRequest)
        // txt +="Complete response: $response\n" // for debug

        // Parsing the request
        if (response.searchResult != null)
        {
            val item: Item? = response.searchResult.items[0]
            if (item != null)
            {
                if (item.detailPageURL != null) {
                    txt += "<a href=\"" + item.detailPageURL + "\" target=\"_blank\">\n"
                }
                if (item.images != null)
                {
                    val imagesURL = item.images.primary.small.url
                    txt += "<img src=\"$imagesURL\">\n"
                }
                if (item.itemInfo != null)
                {
                    txt += item.itemInfo.title.displayValue + "\n"
                }
                txt += "</a>\n"
            }
        }

        if (response.errors != null) {
            txt += "Printing errors:\nPrinting Errors from list of Errors\n"
            for (error in response.errors) {
                txt += "Error code: " + error.code + "\n"
                txt += "Error message: " + error.message + "\n"
            }
        }
    } catch (exception: ApiException) {
        // Exception handling
        txt += "Error calling PA-API 5.0!" + "\n"
        txt += "Status code: " + exception.code + "\n"
        txt += "Errors: " + exception.responseBody + "\n"
        txt += "Message: " + exception.message + "\n"
        if (exception.responseHeaders != null) {
            // Printing request reference
            txt += "Request ID: " + exception.responseHeaders["x-amzn-RequestId"] + "\n"
        }
        // exception.printStackTrace();
    } catch (exception: Exception) {
        txt += "Exception message: " + exception.message  + "\n"
        // exception.printStackTrace();
    }
    return txt
}
