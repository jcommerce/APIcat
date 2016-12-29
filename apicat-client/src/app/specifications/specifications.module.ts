import {NgModule} from "@angular/core";
import {SpecificationDetailsComponent} from "./specification-details/specification-details.component";
import {SpecificationAddComponent} from "./specification-add/specification-add.component";
import {SpecificationFormComponent} from "./specification-form/specification-form.component";
import {SpecificationListComponent} from "./specification-list/specification-list.component";
import {SpecificationEditComponent} from "./specification-edit/specification-edit.component";
import {SpecificationService} from "./specification.service";
import {SharedModule} from "../shared/shared.module";

@NgModule({
  imports: [
    SharedModule
  ],
  declarations:[
    SpecificationListComponent,
    SpecificationFormComponent,
    SpecificationAddComponent,
    SpecificationEditComponent,
    SpecificationDetailsComponent
  ],
  providers: [
    SpecificationService
  ],
  exports: [
  ]
})
export class SpecificationsModule {
}
