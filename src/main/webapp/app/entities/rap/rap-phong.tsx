import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { getSortState, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './rap.reducer';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getEntities, getEntitiesByRapId } from '../phong/phong.reducer';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';

// import React, { useState, useEffect } from 'react';
// import { Link, useLocation, useNavigate } from 'react-router-dom';
// import { Button, Table } from 'reactstrap';
// import { Translate, getSortState } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
// import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
// import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
// import { useAppDispatch, useAppSelector } from 'app/config/store';

// import { getEntities } from './phong.reducer';

export const RapPhong = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const rapEntity = useAppSelector(state => state.rap.entity);

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const phongList = useAppSelector(state => state.phong.entities);
  const loading = useAppSelector(state => state.phong.loading);

  const getAllEntities = () => {
    dispatch(getEntitiesByRapId(id));
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };
  return (
    <div>
      <Row>
        <Col md="8">
          <h2 data-cy="rapDetailsHeading">
            <Translate contentKey="projectMovieApp.rap.detail.title">Rap</Translate>
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="id">
                <Translate contentKey="global.field.id">ID</Translate>
              </span>
            </dt>
            <dd>{rapEntity.id}</dd>
            <dt>
              <span id="tenRap">
                <Translate contentKey="projectMovieApp.rap.tenRap">Ten Rap</Translate>
              </span>
            </dt>
            <dd>{rapEntity.tenRap}</dd>
            <dt>
              <span id="diaChi">
                <Translate contentKey="projectMovieApp.rap.diaChi">Dia Chi</Translate>
              </span>
            </dt>
            <dd>{rapEntity.diaChi}</dd>
            <dt>
              <span id="thanhPho">
                <Translate contentKey="projectMovieApp.rap.thanhPho">Thanh Pho</Translate>
              </span>
            </dt>
            <dd>{rapEntity.thanhPho}</dd>
            <dt>
              <Translate contentKey="projectMovieApp.rap.cumRap">Cum Rap</Translate>
            </dt>
            <dd>{rapEntity.cumRap ? rapEntity.cumRap.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/rap" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/rap/${rapEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
      <div>
        <h2 id="phong-heading" data-cy="PhongHeading">
          <Translate contentKey="projectMovieApp.phong.home.title">Phongs</Translate>
          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} />{' '}
              <Translate contentKey="projectMovieApp.phong.home.refreshListLabel">Refresh List</Translate>
            </Button>
            <Link to="/phong/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="projectMovieApp.phong.home.createLabel">Create new Phong</Translate>
            </Link>
          </div>
        </h2>
        <div className="table-responsive">
          {phongList && phongList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="projectMovieApp.phong.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('tenPhong')}>
                    <Translate contentKey="projectMovieApp.phong.tenPhong">Ten Phong</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('tenPhong')} />
                  </th>
                  <th>
                    <Translate contentKey="projectMovieApp.phong.rap">Rap</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {phongList.map((phong, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/phong/${phong.id}`} color="link" size="sm">
                        {phong.id}
                      </Button>
                    </td>
                    <td>{phong.tenPhong}</td>
                    <td>{phong.rap ? <Link to={`/rap/${phong.rap.id}`}>{phong.rap.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/phong/${phong.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/phong/${phong.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/phong/${phong.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="projectMovieApp.phong.home.notFound">No Phongs found</Translate>
              </div>
            )
          )}
        </div>
      </div>
    </div>
  );
};

export default RapPhong;
