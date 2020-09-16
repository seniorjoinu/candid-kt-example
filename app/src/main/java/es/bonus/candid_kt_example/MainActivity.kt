package es.bonus.candid_kt_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import es.bonus.candid_kt_example.generated.Entry
import es.bonus.candid_kt_example.generated.MainActor
import es.bonus.candid_kt_example.ui.CandidktexampleTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import senior.joinu.candid.transpile.SimpleIDLPrincipal
import senior.joinu.candid.utils.EdDSAKeyPair
import java.math.BigInteger

class MainActivity : AppCompatActivity() {
    val host = "http://10.0.2.2:8000"
    val keys = EdDSAKeyPair.generateInsecure()
    val canisterId = "75hes-oqbaa-aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-q"
    val phonebookActor = MainActor(host, SimpleIDLPrincipal.fromText(canisterId), keys)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CandidktexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val currentLookupEntry = remember { mutableStateOf<Entry?>(null) }

                    Column() {
                        EntryLookupForm(
                            phonebookActor = phonebookActor,
                            onSuccess = { currentLookupEntry.value = it })
                        EntryInsertForm(phonebookActor = phonebookActor)

                        if (currentLookupEntry.value != null) {
                            Column() {
                                Text("Entry found!")
                                EntryView(entry = currentLookupEntry.value!!)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EntryInsertForm(phonebookActor: MainActor) {
    val currentInsertEntry = remember { mutableStateOf(Entry("", "", BigInteger.ZERO)) }

    Column() {
        Text("Add new entry")
        TextField(
            value = currentInsertEntry.value.name,
            onValueChange = { currentInsertEntry.value = currentInsertEntry.value.copy(name = it) },
            label = {
                Text("Name")
            }
        )
        TextField(
            value = currentInsertEntry.value.description,
            onValueChange = { currentInsertEntry.value = currentInsertEntry.value.copy(description = it) },
            label = {
                Text("Description")
            }
        )
        TextField(
            value = currentInsertEntry.value.phone.toString(),
            onValueChange = { currentInsertEntry.value = currentInsertEntry.value.copy(phone = it.toBigInteger()) },
            label = {
                Text("Phone number")
            }
        )

        val coroutineScope = rememberCoroutineScope()
        Button(
            onClick = { coroutineScope.launch {
                phonebookActor.insert(
                    currentInsertEntry.value.name,
                    currentInsertEntry.value.description,
                    currentInsertEntry.value.phone
                )
            } }
        ) {
            Text("Insert")
        }
    }
}

@Composable
fun EntryLookupForm(phonebookActor: MainActor, onSuccess: (entry: Entry) -> Unit) {
    val name = remember { mutableStateOf("") }
    Column() {
        Text("Lookup entry")
        TextField(value = name.value, onValueChange = { name.value = it }, label = {
            Text("Name")
        })
        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = { coroutineScope.launch {
                val entry = phonebookActor.lookup(name.value)
                if (entry != null) onSuccess(entry)
                else println("No such name in phonebook")
            } }
        ) {
            Text("Lookup")
        }
    }
}

@Composable
fun EntryView(entry: Entry) {
    Row() {
        Text(entry.name)
        Text(entry.description)
        Text(entry.phone.toString())
    }
}