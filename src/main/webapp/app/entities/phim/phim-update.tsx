import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPhim } from 'app/shared/model/phim.model';
import { TheLoai } from 'app/shared/model/enumerations/the-loai.model';
import { getEntity, updateEntity, createEntity, reset } from './phim.reducer';

export const PhimUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const phimEntity = useAppSelector(state => state.phim.entity);
  const loading = useAppSelector(state => state.phim.loading);
  const updating = useAppSelector(state => state.phim.updating);
  const updateSuccess = useAppSelector(state => state.phim.updateSuccess);
  const theLoaiValues = Object.keys(TheLoai);

  const handleClose = () => {
    navigate('/phim');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.thoiLuong !== undefined && typeof values.thoiLuong !== 'number') {
      values.thoiLuong = Number(values.thoiLuong);
    }

    const entity = {
      ...phimEntity,
      ...values,
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
          theLoai: 'KINH_DI',
          ...phimEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="projectMovieApp.phim.home.createOrEditLabel" data-cy="PhimCreateUpdateHeading">
            <Translate contentKey="projectMovieApp.phim.home.createOrEditLabel">Create or edit a Phim</Translate>
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
                  id="phim-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('projectMovieApp.phim.tenPhim')}
                id="phim-tenPhim"
                name="tenPhim"
                data-cy="tenPhim"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('projectMovieApp.phim.thoiLuong')}
                id="phim-thoiLuong"
                name="thoiLuong"
                data-cy="thoiLuong"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('projectMovieApp.phim.gioiThieu')}
                id="phim-gioiThieu"
                name="gioiThieu"
                data-cy="gioiThieu"
                type="text"
              />
              <ValidatedField
                label={translate('projectMovieApp.phim.ngayCongChieu')}
                id="phim-ngayCongChieu"
                name="ngayCongChieu"
                data-cy="ngayCongChieu"
                type="date"
              />
              <ValidatedField
                label={translate('projectMovieApp.phim.linkTrailer')}
                id="phim-linkTrailer"
                name="linkTrailer"
                data-cy="linkTrailer"
                type="text"
              />
              <ValidatedBlobField
                label={translate('projectMovieApp.phim.logo')}
                id="phim-logo"
                name="logo"
                data-cy="logo"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('projectMovieApp.phim.doTuoi')}
                id="phim-doTuoi"
                name="doTuoi"
                data-cy="doTuoi"
                type="text"
              />
              <ValidatedField
                label={translate('projectMovieApp.phim.theLoai')}
                id="phim-theLoai"
                name="theLoai"
                data-cy="theLoai"
                type="select"
              >
                {theLoaiValues.map(theLoai => (
                  <option value={theLoai} key={theLoai}>
                    {translate('projectMovieApp.TheLoai.' + theLoai)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('projectMovieApp.phim.dinhDang')}
                id="phim-dinhDang"
                name="dinhDang"
                data-cy="dinhDang"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/phim" replace color="info">
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

export default PhimUpdate;
