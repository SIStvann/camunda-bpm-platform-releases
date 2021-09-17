--
-- Copyright © 2012 - 2018 camunda services GmbH and various authors (info@camunda.com)
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

drop index ACT_IDX_CASE_EXEC_BUSKEY;
drop index ACT_IDX_CASE_DEF_TENANT_ID;
drop index ACT_IDX_CASE_EXEC_TENANT_ID;

alter table ACT_RU_CASE_EXECUTION
    drop constraint ACT_FK_CASE_EXE_CASE_INST;

alter table ACT_RU_CASE_EXECUTION
    drop constraint ACT_FK_CASE_EXE_PARENT;

alter table ACT_RU_CASE_EXECUTION
    drop constraint ACT_FK_CASE_EXE_CASE_DEF;

alter table ACT_RU_VARIABLE
    drop constraint ACT_FK_VAR_CASE_EXE;

alter table ACT_RU_VARIABLE
    drop constraint ACT_FK_VAR_CASE_INST;

alter table ACT_RU_TASK
    drop constraint ACT_FK_TASK_CASE_EXE;

alter table ACT_RU_TASK
    drop constraint ACT_FK_TASK_CASE_DEF;

alter table ACT_RU_CASE_SENTRY_PART
    drop constraint ACT_FK_CASE_SENTRY_CASE_INST;

alter table ACT_RU_CASE_SENTRY_PART
    drop constraint ACT_FK_CASE_SENTRY_CASE_EXEC;

drop table ACT_RE_CASE_DEF if exists;
drop table ACT_RU_CASE_EXECUTION if exists;
drop table ACT_RU_CASE_SENTRY_PART if exists;