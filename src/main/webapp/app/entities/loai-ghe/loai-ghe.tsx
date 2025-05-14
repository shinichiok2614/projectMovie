import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './loai-ghe.reducer';

export const LoaiGhe = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const loaiGheList = useAppSelector(state => state.loaiGhe.entities);
  const loading = useAppSelector(state => state.loaiGhe.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
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
      <h2 id="loai-ghe-heading" data-cy="LoaiGheHeading">
        <Translate contentKey="projectMovieApp.loaiGhe.home.title">Loai Ghes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="projectMovieApp.loaiGhe.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/loai-ghe/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="projectMovieApp.loaiGhe.home.createLabel">Create new Loai Ghe</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {loaiGheList && loaiGheList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="projectMovieApp.loaiGhe.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('tenLoai')}>
                  <Translate contentKey="projectMovieApp.loaiGhe.tenLoai">Ten Loai</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tenLoai')} />
                </th>
                <th className="hand" onClick={sort('giaTien')}>
                  <Translate contentKey="projectMovieApp.loaiGhe.giaTien">Gia Tien</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('giaTien')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {loaiGheList.map((loaiGhe, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/loai-ghe/${loaiGhe.id}`} color="link" size="sm">
                      {loaiGhe.id}
                    </Button>
                  </td>
                  <td>{loaiGhe.tenLoai}</td>
                  <td>{loaiGhe.giaTien}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/loai-ghe/${loaiGhe.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/loai-ghe/${loaiGhe.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/loai-ghe/${loaiGhe.id}/delete`)}
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
              <Translate contentKey="projectMovieApp.loaiGhe.home.notFound">No Loai Ghes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default LoaiGhe;
