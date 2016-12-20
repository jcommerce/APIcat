import "./rxjs-extensions";
import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app-routing.module";
import {DefinitionListComponent} from "./definitions/definition-list/definition-list.component";
import {DefinitionService} from "./definitions/definition.service";
import {DefinitionDetailsComponent} from "./definitions/definition-details/definition-details.component";

@NgModule({
  declarations: [
    AppComponent,
    DefinitionListComponent,
    DefinitionDetailsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
  ],
  providers: [DefinitionService],
  bootstrap: [AppComponent]
})
export class AppModule {

}


