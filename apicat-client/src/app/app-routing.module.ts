import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {DefinitionListComponent} from "./definitions/definition-list/definition-list.component";
import {DefinitionDetailsComponent} from "./definitions/definition-details/definition-details.component";
import {DefinitionAddComponent} from "./definitions/definition-add/definition-add.component";
import {DefinitionEditComponent} from "./definitions/definition-edit/definition-edit.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {SpecificationListComponent} from "./specifications/specification-list/specification-list.component";
import {SpecificationAddComponent} from "./specifications/specification-add/specification-add.component";
import {SpecificationEditComponent} from "./specifications/specification-edit/specification-edit.component";
import {SpecificationDetailsComponent} from "./specifications/specification-details/specification-details.component";

const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'definitions', component: DefinitionListComponent},
  {path: 'definitions/add', component: DefinitionAddComponent},
  {path: 'definitions/:id/edit', component: DefinitionEditComponent},
  {path: 'definitions/:id', component: DefinitionDetailsComponent},
  {path: 'specifications', component: SpecificationListComponent},
  {path: 'specifications/add', component: SpecificationAddComponent},
  {path: 'specifications/:id/edit', component: SpecificationEditComponent},
  {path: 'specifications/:id', component: SpecificationDetailsComponent},
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
