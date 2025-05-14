import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPhong } from 'app/shared/model/phong.model';
import { getEntities as getPhongs } from 'app/entities/phong/phong.reducer';
import { IVe } from 'app/shared/model/ve.model';
import { getEntities as getVes } from 'app/entities/ve/ve.reducer';
import { ILoaiGhe } from 'app/shared/model/loai-ghe.model';
import { getEntities as getLoaiGhes } from 'app/entities/loai-ghe/loai-ghe.reducer';
import { IGhe } from 'app/shared/model/ghe.model';
import { TinhTrangGhe } from 'app/shared/model/enumerations/tinh-trang-ghe.model';
import { getEntity, updateEntity, createEntity, reset } from './ghe.reducer';

export const GheUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const phongs = useAppSelector(state => state.phong.entities);
  const ves = useAppSelector(state => state.ve.entities);
  const loaiGhes = useAppSelector(state => state.loaiGhe.entities);
  const gheEntity = useAppSelector(state => state.ghe.entity);
  const loading = useAppSelector(state => state.ghe.loading);
  const updating = useAppSelector(state => state.ghe.updating);
  const updateSuccess = useAppSelector(state => state.ghe.updateSuccess);
  const tinhTrangGheValues = Object.keys(TinhTrangGhe);

  const handleClose = () => {
    navigate('/ghe');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPhongs({}));
    dispatch(getVes({}));
    dispatch(getLoaiGhes({}));
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
      ...gheEntity,
      ...values,
      phong: phongs.find(it => it.id.toString() === values.phong?.toString()),
      ve: ves.find(it => it.id.toString() === values.ve?.toString()),
      loaiGhe: loaiGhes.find(it => it.id.toString() === values.loaiGhe?.toString()),
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
          tinhTrang: 'TRONG',
          ...gheEntity,
          phong: gheEntity?.phong?.id,
          ve: gheEntity?.ve?.id,
          loaiGhe: gheEntity?.loaiGhe?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="projectMovieApp.ghe.home.createOrEditLabel" data-cy="GheCreateUpdateHeading">
            <Translate contentKey="projectMovieApp.ghe.home.createOrEditLabel">Create or edit a Ghe</Translate>
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
                  id="ghe-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('projectMovieApp.ghe.tenGhe')}
                id="ghe-tenGhe"
                name="tenGhe"
                data-cy="tenGhe"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('projectMovieApp.ghe.tinhTrang')}
                id="ghe-tinhTrang"
                name="tinhTrang"
                data-cy="tinhTrang"
                type="select"
              >
                {tinhTrangGheValues.map(tinhTrangGhe => (
                  <option value={tinhTrangGhe} key={tinhTrangGhe}>
                    {translate('projectMovieApp.TinhTrangGhe.' + tinhTrangGhe)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="ghe-phong" name="phong" data-cy="phong" label={translate('projectMovieApp.ghe.phong')} type="select">
                <option value="" key="0" />
                {phongs
                  ? phongs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="ghe-ve" name="ve" data-cy="ve" label={translate('projectMovieApp.ghe.ve')} type="select">
                <option value="" key="0" />
                {ves
                  ? ves.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ghe-loaiGhe"
                name="loaiGhe"
                data-cy="loaiGhe"
                label={translate('projectMovieApp.ghe.loaiGhe')}
                type="select"
              >
                <option value="" key="0" />
                {loaiGhes
                  ? loaiGhes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ghe" replace color="info">
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

export default GheUpdate;
