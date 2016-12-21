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
import {DefinitionFormComponent} from "./definitions/definition-form/definition-form.component";
import {CollapseDirective} from "ng2-bootstrap";
import {DefinitionAddComponent} from "./definitions/definition-add/definition-add.component";
import {InMemoryDataService} from "./shared/in-memory-data.service";
import {InMemoryWebApiModule} from "angular-in-memory-web-api";

@NgModule({
  declarations: [
    //bootstrap
    CollapseDirective,
    //app declarations
    AppComponent,
    DefinitionAddComponent,
    DefinitionListComponent,
    DefinitionFormComponent,
    DefinitionDetailsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    InMemoryWebApiModule.forRoot(InMemoryDataService),
    AppRoutingModule,
  ],
  providers: [DefinitionService],
  bootstrap: [AppComponent]
})
export class AppModule {

}


