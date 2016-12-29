import {NgModule} from "@angular/core";
import {SharedModule} from "../shared/shared.module";
import {DefinitionListComponent} from "./definition-list/definition-list.component";
import {DefinitionFormComponent} from "./definition-form/definition-form.component";
import {DefinitionAddComponent} from "./definition-add/definition-add.component";
import {DefinitionEditComponent} from "./definition-edit/definition-edit.component";
import {DefinitionDetailsComponent} from "./definition-details/definition-details.component";
import {DefinitionService} from "./definition.service";

@NgModule({
  imports: [
    SharedModule
  ],
  declarations:[
    DefinitionListComponent,
    DefinitionFormComponent,
    DefinitionAddComponent,
    DefinitionEditComponent,
    DefinitionDetailsComponent
  ],
  providers: [
    DefinitionService
  ],
  exports: [
  ]
})
export class DefinitionsModule {
}
