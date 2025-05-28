import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { getSortState, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './phong.reducer';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getEntities, getEntitiesByPhongId } from '../ghe/ghe.reducer';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';

export const PhongGhe = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const phongEntity = useAppSelector(state => state.phong.entity);

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const gheList = useAppSelector(state => state.ghe.entities);
  const loading = useAppSelector(state => state.ghe.loading);

  const getAllEntities = () => {
    dispatch(getEntitiesByPhongId(id));
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
          <h2 data-cy="phongDetailsHeading">
            <Translate contentKey="projectMovieApp.phong.detail.title">Phong</Translate>
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="id">
                <Translate contentKey="global.field.id">ID</Translate>
              </span>
            </dt>
            <dd>{phongEntity.id}</dd>
            <dt>
              <span id="tenPhong">
                <Translate contentKey="projectMovieApp.phong.tenPhong">Ten Phong</Translate>
              </span>
            </dt>
            <dd>{phongEntity.tenPhong}</dd>
            <dt>
              <Translate contentKey="projectMovieApp.phong.rap">Rap</Translate>
            </dt>
            <dd>{phongEntity.rap ? phongEntity.rap.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/phong" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/phong/${phongEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
      <div>
        <h2 id="ghe-heading" data-cy="GheHeading">
          <Translate contentKey="projectMovieApp.ghe.home.title">Ghes</Translate>
          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} />{' '}
              <Translate contentKey="projectMovieApp.ghe.home.refreshListLabel">Refresh List</Translate>
            </Button>
            <Link to="/ghe/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="projectMovieApp.ghe.home.createLabel">Create new Ghe</Translate>
            </Link>
          </div>
        </h2>
        <div className="table-responsive">
          {gheList && gheList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="projectMovieApp.ghe.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('tenGhe')}>
                    <Translate contentKey="projectMovieApp.ghe.tenGhe">Ten Ghe</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('tenGhe')} />
                  </th>
                  <th className="hand" onClick={sort('tinhTrang')}>
                    <Translate contentKey="projectMovieApp.ghe.tinhTrang">Tinh Trang</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('tinhTrang')} />
                  </th>
                  <th>
                    <Translate contentKey="projectMovieApp.ghe.phong">Phong</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="projectMovieApp.ghe.ve">Ve</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="projectMovieApp.ghe.loaiGhe">Loai Ghe</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {gheList.map((ghe, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/ghe/${ghe.id}`} color="link" size="sm">
                        {ghe.id}
                      </Button>
                    </td>
                    <td>{ghe.tenGhe}</td>
                    <td>
                      <Translate contentKey={`projectMovieApp.TinhTrangGhe.${ghe.tinhTrang}`} />
                    </td>
                    <td>{ghe.phong ? <Link to={`/phong/${ghe.phong.id}`}>{ghe.phong.id}</Link> : ''}</td>
                    <td>{ghe.ve ? <Link to={`/ve/${ghe.ve.id}`}>{ghe.ve.id}</Link> : ''}</td>
                    <td>{ghe.loaiGhe ? <Link to={`/loai-ghe/${ghe.loaiGhe.id}`}>{ghe.loaiGhe.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/ghe/${ghe.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/ghe/${ghe.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/ghe/${ghe.id}/delete`)}
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
                <Translate contentKey="projectMovieApp.ghe.home.notFound">No Ghes found</Translate>
              </div>
            )
          )}
        </div>
      </div>
    </div>
  );
};

export default PhongGhe;
