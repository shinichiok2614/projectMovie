import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVe } from 'app/shared/model/ve.model';
import { getEntities as getVes } from 'app/entities/ve/ve.reducer';
import { IDanhSachBapNuoc } from 'app/shared/model/danh-sach-bap-nuoc.model';
import { getEntity, updateEntity, createEntity, reset } from './danh-sach-bap-nuoc.reducer';

export const DanhSachBapNuocUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ves = useAppSelector(state => state.ve.entities);
  const danhSachBapNuocEntity = useAppSelector(state => state.danhSachBapNuoc.entity);
  const loading = useAppSelector(state => state.danhSachBapNuoc.loading);
  const updating = useAppSelector(state => state.danhSachBapNuoc.updating);
  const updateSuccess = useAppSelector(state => state.danhSachBapNuoc.updateSuccess);

  const handleClose = () => {
    navigate('/danh-sach-bap-nuoc');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getVes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...danhSachBapNuocEntity,
      ...values,
      ve: ves.find(it => it.id.toString() === values.ve?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...danhSachBapNuocEntity,
          ve: danhSachBapNuocEntity?.ve?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="projectMovieApp.danhSachBapNuoc.home.createOrEditLabel" data-cy="DanhSachBapNuocCreateUpdateHeading">
            <Translate contentKey="projectMovieApp.danhSachBapNuoc.home.createOrEditLabel">Create or edit a DanhSachBapNuoc</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="danh-sach-bap-nuoc-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('projectMovieApp.danhSachBapNuoc.soDienThoai')}
                id="danh-sach-bap-nuoc-soDienThoai"
                name="soDienThoai"
                data-cy="soDienThoai"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('projectMovieApp.danhSachBapNuoc.tenBapNuoc')}
                id="danh-sach-bap-nuoc-tenBapNuoc"
                name="tenBapNuoc"
                data-cy="tenBapNuoc"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="danh-sach-bap-nuoc-ve"
                name="ve"
                data-cy="ve"
                label={translate('projectMovieApp.danhSachBapNuoc.ve')}
                type="select"
              >
                <option value="" key="0" />
                {ves
                  ? ves.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/danh-sach-bap-nuoc" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DanhSachBapNuocUpdate;
