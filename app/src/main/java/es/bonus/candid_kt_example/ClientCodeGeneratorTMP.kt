package es.bonus.candid_kt_example

import senior.joinu.candid.CandidCodeGenerator
import java.nio.file.Paths

fun main() {
    val didPath = Paths.get("").resolve("backend/.dfx/local/canisters/phonebook/phonebook.did")
    val ktPath = Paths.get("").resolve("app/src/main/java/es/bonus/candid_kt_example/generated")

    val src = CandidCodeGenerator.Source.File(didPath, didPath.fileName.toString())
    CandidCodeGenerator.generate(src, ktPath,"es.bonus.candid_kt_example.generated")
}