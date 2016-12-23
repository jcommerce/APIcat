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
import {CollapseDirective, AlertModule, AlertConfig} from "ng2-bootstrap";
import {DefinitionAddComponent} from "./definitions/definition-add/definition-add.component";
import {InMemoryDataService} from "./common/in-memory-data.service";
import {InMemoryWebApiModule} from "angular-in-memory-web-api";
import {DefinitionEditComponent} from "./definitions/definition-edit/definition-edit.component";
import {ConfirmationModalComponent} from "./common/confirmation-modal/confirmation-modal.component";
import {AlertMessageService} from "./common/alert/alert-message.service";
import {InMemoryApiFormatService} from "./common/in-memory-api-format.service";
import {ApiFormatService} from "./common/api-format.service";

@NgModule({
  declarations: [
    //bootstrap
    CollapseDirective,
    //app declarations
    AppComponent,
    DefinitionAddComponent,
    DefinitionEditComponent,
    DefinitionListComponent,
    DefinitionFormComponent,
    DefinitionDetailsComponent,
    ConfirmationModalComponent
  ],
  imports: [
    AlertModule,
    BrowserModule,
    FormsModule,
    HttpModule,
    InMemoryWebApiModule.forRoot(InMemoryDataService),
    AppRoutingModule,
  ],
  providers: [
    DefinitionService,
    AlertMessageService,
    ApiFormatService,
    AlertConfig
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}


