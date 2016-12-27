import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {DefinitionListComponent} from "./definitions/definition-list/definition-list.component";
import {DefinitionDetailsComponent} from "./definitions/definition-details/definition-details.component";
import {DefinitionAddComponent} from "./definitions/definition-add/definition-add.component";
import {DefinitionEditComponent} from "./definitions/definition-edit/definition-edit.component";
import {DashboardComponent} from "./dashboard/dashboard.component";

const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'definitions', component: DefinitionListComponent},
  {path: 'definitions/add', component: DefinitionAddComponent},
  {path: 'definitions/:id/edit', component: DefinitionEditComponent},
  {path: 'definitions/:id', component: DefinitionDetailsComponent},
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
